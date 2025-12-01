package com.example.chatserver.member.service;

import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberListRequestDto;
import com.example.chatserver.member.dto.MemberLoginRequestDto;
import com.example.chatserver.member.dto.MemberSaveRequestDto;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(MemberSaveRequestDto memberSaveRequestDto) {

        if (memberRepository.findByEmail(memberSaveRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member newMember = Member.builder()
                .name(memberSaveRequestDto.getName())
                .email(memberSaveRequestDto.getEmail())
                .password(passwordEncoder.encode(memberSaveRequestDto.getPassword()))
                .build();

        return  memberRepository.save(newMember);
    }

    public Member login(MemberLoginRequestDto memberLoginRequestDto){

        Member member = memberRepository.findByEmail(memberLoginRequestDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일 입니다."));

        if (!passwordEncoder.matches(memberLoginRequestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("아이디와 비밀번호를 다시 확인해주세요");
        }

        return member;
    }

    public List<MemberListRequestDto> findAll() {

        return memberRepository.findAll().stream()
                .map(member -> new MemberListRequestDto(
                        member.getId(),
                        member.getName(),
                        member.getEmail()
                ))
                .toList();
    }
}
