package com.wei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.entity.AddressBook;
import com.wei.mapper.AddressBookMapper;
import com.wei.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年09月10日 5:49 PM
 */

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService {
}
