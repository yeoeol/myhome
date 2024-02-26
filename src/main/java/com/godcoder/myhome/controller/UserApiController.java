package com.godcoder.myhome.controller;

import com.godcoder.myhome.mapper.UserMapper;
import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.QUser;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@RestController
class UserApiController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @GetMapping("/users")
    Iterable<User> all(@RequestParam(required = false) String method,
                   @RequestParam(required = false) String text) {
        Iterable<User> users = null;
        if ("query".equals(method)) {
            users = userRepository.findByUsernameQuery(text);
        } else if ("nativeQuery".equals(method)) {
            users = userRepository.findByUsernameNativeQuery(text);
        } else if ("querydsl".equals(method)) {
            QUser user = QUser.user;
            Predicate predicate = user.username.contains(text);

            users = userRepository.findAll(predicate);
        } else if ("querydslCustom".equals(method)) {
            users = userRepository.findByUsernameCustom(text);
        } else if ("mybatis".equals(method)) {
            users = userMapper.getUsers(text);
        } else {
            users = userRepository.findAll();
        }
        return users;
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return userRepository.findById(id)
                .map(user -> {
//                    User.setTitle(newUser.getTitle());
//                    User.setContent(newUser.getContent());
                    user.getBoards().clear();
                    user.getBoards().addAll(newUser.getBoards());
                    for (Board board : user.getBoards()) {
                        board.setUser(user);
                    }
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
