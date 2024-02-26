package com.godcoder.myhome.service;

import com.godcoder.myhome.config.WebSecurityConfig;
import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.Role;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.BoardRepository;
import com.godcoder.myhome.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private PasswordEncoder encoder = WebSecurityConfig.passwordEncoder();

    @Transactional
    public User save(User user) {
        String encodePassword = encoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setEnabled(true);

        Role role = new Role();
        role.setId(1L);
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        // 사용자 가입인사글 자동작성
        Board board = new Board();
        board.setTitle("안녕하세요!");
        board.setContent("반갑습니다.");
        board.setUser(savedUser);
        boardRepository.save(board);

        return savedUser;
    }
}
