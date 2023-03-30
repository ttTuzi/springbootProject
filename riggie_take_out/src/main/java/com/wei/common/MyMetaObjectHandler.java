package com.wei.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年03月04日 7:09 PM
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
      log.info("自动填充[insert]");
      log.info(metaObject.toString());
      metaObject.setValue("createTime", LocalDateTime.now());
      metaObject.setValue("updateTime", LocalDateTime.now());
      metaObject.setValue("createUser", BaseContext.getCurrentId());
      metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("自动填充[update]");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

    }
}
