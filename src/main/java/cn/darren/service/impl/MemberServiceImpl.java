package cn.darren.service.impl;

import cn.darren.entity.Member;
import cn.darren.entity.MemberReadState;
import cn.darren.exception.BussinessException;
import cn.darren.mapper.MemberMapper;
import cn.darren.mapper.MemberReadStateMapper;
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
    @Resource
    private MemberReadStateMapper memberReadStateMapper;
    
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

    /**
     * 登录检查
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录对象
     */
    @Override
    public Member checkLogin(String username, String password) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("username", username);
        Member member = memberMapper.selectOne(queryWrapper);
        //判断用户是否存在
        if(member == null){
            throw new BussinessException("MO2", "用户不存在");
        }
        //判断输入的用户密码是否正确
        String md5 = MD5Utils.md5Digest(password, member.getSalt());
        if(!md5.equals(member.getPassword())){
            throw new BussinessException("M03", "输入密码有误");
        }
        return member;
    }

    /**
     * 获取阅读状态
     *
     * @param memberId 会员编号
     * @param bookId   图书编号
     * @return 阅读状态对象
     */
    @Override
    public MemberReadState selectMemberReadState(Long memberId, Long bookId) {
        QueryWrapper<MemberReadState> queryWrapper = new QueryWrapper<MemberReadState>();
        queryWrapper.eq("book_id", bookId);
        queryWrapper.eq("member_id", memberId);
        MemberReadState memberReadState = memberReadStateMapper.selectOne(queryWrapper);
        return memberReadState;
    }
}
