package com.krupoderov.spring.articles.service;

import com.krupoderov.spring.articles.model.User;
import com.krupoderov.spring.articles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Класс, представляющий собой сервис(компонент),
 * в котором мы описываем необходимые нам методы для работы со моделью User
 *
 * @version 1.0
 * @author Krupoderov Mikhail
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * Метод для поиска пользователя по имени
     * @param username имя пользователя
     * @return возвращает найденного в базе пользователя
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Can't find user with username " + username);
        }
        return user;
    }

    public void updateProfile(User user, String password) {
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepository.save(user);
    }

    public User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (userDetails instanceof User) {
            user = (User) userDetails;
        }
        return user;
    }
}
