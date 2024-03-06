package com.ruihang.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruihang.common.R;
import com.ruihang.entity.Employee;
import com.ruihang.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //员工登录
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        //1.将用户输入的密码进行md5加密处理.
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据用户名查数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.没查询到返回失败
        if (emp == null) {
            return R.error("没有该用户,登录失败");
        }

        //4.用户名存在，通过密码比对，如果不一致返回登录失败
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        //5.到这里表示用户名和密码都正确，需要判断员工状态（status）
        if (emp.getStatus() == 0) {
            return R.error("该员工的账号已经禁用");
        }

        //6.都判断完表示登录成功,将员工ID存入session然后返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    //员工退出
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {

        //1.清理session中存储的的当前员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    //新增员工
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        //设置初始密码，但需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employeeService.save(employee);
        return R.success("新增员工成功");
    }


    //分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);

    }


    //修改员工信息
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){

        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    //通过id查询员工，回显部分
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){

        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }else{
            return R.error("查询失败");
        }

    }

}
