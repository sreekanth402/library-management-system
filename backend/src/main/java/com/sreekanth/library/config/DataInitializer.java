
package com.sreekanth.library.config;

import com.sreekanth.library.entity.Book;
import com.sreekanth.library.entity.Member;
import com.sreekanth.library.entity.MemberStatus;
import com.sreekanth.library.entity.Role;
import com.sreekanth.library.entity.User;
import com.sreekanth.library.repository.BookRepository;
import com.sreekanth.library.repository.MemberRepository;
import com.sreekanth.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository,
            MemberRepository memberRepository,
            BookRepository bookRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@library.local")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .build());

            userRepository.save(User.builder()
                    .username("librarian")
                    .email("librarian@library.local")
                    .password(passwordEncoder.encode("Librarian@123"))
                    .role(Role.LIBRARIAN)
                    .build());

            User memberUser = userRepository.save(User.builder()
                    .username("member")
                    .email("sreekanth@library.local")
                    .password(passwordEncoder.encode("Member@123"))
                    .role(Role.MEMBER)
                    .build());

            memberRepository.save(Member.builder()
                    .name("Sreekanth")
                    .email(memberUser.getEmail())
                    .phone("9876543210")
                    .address("Hyderabad")
                    .membershipDate(LocalDate.now())
                    .status(MemberStatus.ACTIVE)
                    .user(memberUser)
                    .build());

            memberRepository.save(Member.builder()
                    .name("Walk-in Member")
                    .email("walkin@library.local")
                    .phone("9000000000")
                    .address("Library desk")
                    .membershipDate(LocalDate.now())
                    .status(MemberStatus.ACTIVE)
                    .build());

            bookRepository.save(Book.builder()
                    .title("Clean Code")
                    .author("Robert C. Martin")
                    .isbn("9780132350884")
                    .category("Programming")
                    .publisher("Prentice Hall")
                    .publicationYear(2008)
                    .totalQuantity(10)
                    .availableQuantity(10)
                    .build());

            bookRepository.save(Book.builder()
                    .title("Effective Java")
                    .author("Joshua Bloch")
                    .isbn("9780134685991")
                    .category("Programming")
                    .publisher("Addison-Wesley")
                    .publicationYear(2018)
                    .totalQuantity(5)
                    .availableQuantity(5)
                    .build());

            bookRepository.save(Book.builder()
                    .title("Java: The Complete Reference")
                    .author("Herbert Schildt")
                    .isbn("9781260463415")
                    .category("Programming")
                    .publisher("McGraw-Hill")
                    .publicationYear(2020)
                    .totalQuantity(8)
                    .availableQuantity(8)
                    .build());
        };
    }
}
