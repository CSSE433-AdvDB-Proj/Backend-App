package com.csse433.blackboard.auth.service.impl;

import com.csse433.blackboard.auth.dao.AuthDao;
import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.pojos.cassandra.UserEntity;
import com.csse433.blackboard.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author chetzhang
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private AuthDao authDao;

    @Override
    public UserAccountDto findUserByToken(String token) {
        String username = authDao.getUsernameByToken(token);
        UserEntity userEntity = authDao.getUserByUsername(username);
        if (userEntity == null) {
            return null;
        }
        UserAccountDto userAccountDto = new UserAccountDto();
        BeanUtils.copyProperties(userEntity, userAccountDto);
        return userAccountDto;
    }

    @Override
    public void extendExpireTime(String token) {
        authDao.extendExpireTime(token);
    }

    @Override
    public void deleteToken(String token) { authDao.deleteToken(token); }


    @Override
    public boolean registerUser(UserAccountDto userAccountDto) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userAccountDto, userEntity);
        String password = userAccountDto.getPassword();
        String salt = generateSalt();
        userEntity.setPasswordHash(encryptWithSalt(password, salt));
        userEntity.setPasswordSalt(salt);
        return authDao.createUser(userEntity);
    }

    @Override
    public boolean updateUser(String token, UserAccountDto userAccountDto) {
        UserEntity userEntity = authDao.getUserByUsername(authDao.getUsernameByToken(token));
        Field[] fields = userAccountDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String fieldValue = (String) field.get(userAccountDto);
                if(!StringUtils.isBlank(fieldValue)) {
                    switch (field.getName()) {
                        case "firstName":
                            userEntity.setFirstName(userAccountDto.getFirstName());
                            break;
                        case "lastName":
                            userEntity.setLastName(userAccountDto.getLastName());
                            break;
                        case "nickname":
                            userEntity.setNickname(userAccountDto.getNickname());
                            break;
                        case "email":
                            userEntity.setEmail(userAccountDto.getEmail());
                            break;
                            // TODO: Reset password? New route?
                        default:
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return authDao.updateUser(userEntity);
    }

    @Override
    public boolean login(String username, String password, HttpServletResponse response) {
        UserEntity userEntity = authDao.getUserByUsername(username);
        if (userEntity == null) {
            return false;
        }
        String passwordHash = userEntity.getPasswordHash();
        String passwordSalt = userEntity.getPasswordSalt();

        String providedPassword = encryptWithSalt(password, passwordSalt);
        boolean correct = providedPassword.equals(passwordHash);
        if(correct){
            String newToken = TokenUtil.token(username);
            authDao.setNewToken(username, newToken);
            response.setHeader(Constants.TOKEN_HEADER, newToken);
            return true;
        }
        return false;
    }



    /**
     * Encrypt password with salt.
     * @param password
     * @param salt
     * @return The encrypted password.
     */
    private String encryptWithSalt(String password, String salt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        if (salt != null) {
            md.update(salt.getBytes());
        }
        md.update(password.getBytes());

        byte[] bytes = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return sb.toString();

    }


    private final static String STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Randomly generates a salt.
     * @return
     */
    private String generateSalt() {
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            char ch = STR.charAt(new Random().nextInt(STR.length()));
            salt.append(ch);
        }
        return salt.toString();
    }


}
