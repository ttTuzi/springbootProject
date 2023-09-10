package com.wei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wei.common.BaseContext;
import com.wei.common.R;
import com.wei.entity.AddressBook;
import com.wei.service.AddressBookService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService service;

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook, HttpSession session) {           //新增地址
        //Long user_id = BaseContext.getCurrentId();           //单线程获取本页面user的id
        Long user_id = (Long) session.getAttribute("user");
        addressBook.setUserId(user_id);

        service.save(addressBook);

        return R.success("新增地址");
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook, HttpSession session) {
//        Long user_id = BaseContext.getCurrentId();           //单线程获取本页面user的id
        Long user_id = (Long) session.getAttribute("user");
        addressBook.setUserId(user_id);

        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();

        //当前用户的所有地址
        wrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        wrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = service.list(wrapper);

        return R.success(list);
    }


    @PutMapping("/default")
    public R<AddressBook> updateDefaultAddress(@RequestBody AddressBook addressBook) {
        log.info("{}",addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();

        //将所有地址状态设置为0
        wrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        service.update(wrapper);

        //修改当前地址的isDefault属性
        addressBook.setIsDefault(1);
        service.updateById(addressBook);

        return R.success(addressBook);
    }

    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id) {           //修改地址时回显数据
        AddressBook addressBook = service.getById(id);
        if(addressBook!=null){
            return R.success(addressBook);
        }else{
            return R.error("No Object Found");
        }

    }

    @DeleteMapping
    public R<String> removeById(Long ids) {                         //删除地址
        service.removeById(ids);
        return R.success("删除成功");
    }

    @PutMapping
    public R<AddressBook> update(@RequestBody AddressBook addressBook) {
        log.info("{}", addressBook);

        //修改地址后设为默认地址
        return updateDefaultAddress(addressBook);
    }

    @GetMapping("/default")
    public R<AddressBook> displayDefaultAddress() {
        //下单时显示默认地址
        Long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,userId);
        wrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook defaultAdd = service.getOne(wrapper);

        if (defaultAdd == null) {
            return R.error("未找到地址");
        }
        return R.success(defaultAdd);
    }
}
