package com.example.chatserver.member.controller;

import com.example.chatserver.common.auth.JwtTokenProvider;
import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberListRequestDto;
import com.example.chatserver.member.dto.MemberLoginRequestDto;
import com.example.chatserver.member.dto.MemberSaveRequestDto;
import com.example.chatserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {
       Member member = memberService.create(memberSaveRequestDto);
       return new ResponseEntity<>(member.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        Member member = memberService.login(memberLoginRequestDto);

        String jwtToken = jwtTokenProvider.createToken(member.getEmail(),member.getRole().toString());
        Map<String,Object> loginInfo = new HashMap<>();
        loginInfo.put("id",member.getId());
        loginInfo.put("token",jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);

    }

    @GetMapping("/list")
    public ResponseEntity<?> memberList() {
        List<MemberListRequestDto> dtos = memberService.findAll();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
