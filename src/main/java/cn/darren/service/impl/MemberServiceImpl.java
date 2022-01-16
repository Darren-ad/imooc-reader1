package cn.darren.service.impl;

import cn.darren.entity.Member;
import cn.darren.exception.BussinessException;
import cn.darren.mapper.MemberMapper;
import cn.darren.service.MemberService;
import cn.darren.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("memberService")
@Transactional
public class MemberServiceImpl implements MemberService {
    
    @Resource
    private MemberMapper memberMapper;
    
    /**
     * 会员注册，创建新会员
     *
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @return 新会员对象
     */
    @Override
    public Member createMember(String username, String password, String nickname) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("username", username);
        List<Member> memberList = memberMapper.selectList(queryWrapper);
        //判断用户名是否已存在
        if(memberList.size() > 0){
            throw new BussinessException("M01", "用户名已存在");
        }
        //用户名不存在，即新用户
        Member member = new Member();
        member.setUsername(username);
        member.setNickname(nickname);
        //将密码进行加密
        int salt = new Random().nextInt(1000) + 1000;//盐值
        String md5 = MD5Utils.md5Digest(password, salt);
        
        member.setPassword(md5);
        member.setSalt(salt);
        member.setCreateTime(new Date());
        memberMapper.insert(member);
        return member;
    }
}
