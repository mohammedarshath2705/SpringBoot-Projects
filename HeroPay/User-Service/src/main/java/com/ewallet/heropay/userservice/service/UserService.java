package com.ewallet.heropay.userservice.service;


import com.ewallet.heropay.userservice.model.User;
import com.ewallet.heropay.userservice.repository.UserCacheRepository;
import com.ewallet.heropay.userservice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private static final String USER_CREATE_TOPIC= "user_created";


   private final UserRepository userRepository;
   private final UserCacheRepository userCacheRepository;
   private final KafkaTemplate<String,String> kafkaTemplate;

   @Autowired
    public UserService(UserRepository userRepository, UserCacheRepository userCacheRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.userCacheRepository = userCacheRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void create(User user) throws JsonProcessingException {

        userRepository.save(user);
        //userCacheRepository.set(user);

        JSONObject userObj= new JSONObject();
        userObj.put("phone",user.getPhone());
        userObj.put("email",user.getEmail());
        kafkaTemplate.send(USER_CREATE_TOPIC,this.objectMapper.writeValueAsString(userObj));
    }
    public User get(int userId) throws Exception {

        User user =userCacheRepository.get(userId);
        if(user != null){
            return user;
        }
        user =userRepository.findById(userId).orElseThrow(() -> new Exception());
        userCacheRepository.set(user);
        return user;
    }

    public User getByPhone(String phone) throws Exception {
        return userRepository.findByPhone(phone);
    }
}
