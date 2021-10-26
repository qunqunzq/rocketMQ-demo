package com.roy.rocketmq.config;

import com.alibaba.fastjson.JSONObject;
import com.roy.rocketmq.domain.User;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.StringMessageConverter;

import java.util.Map;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/10/26 10:19 上午
 */
@RocketMQTransactionListener
public class QunTransactionImpl implements RocketMQLocalTransactionListener {
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        final org.apache.rocketmq.common.message.Message message = RocketMQUtil.convertToRocketMessage(new StringMessageConverter(), "UTF-8", arg.toString(), msg);
        final byte[] body = message.getBody();
        final User o = JSONObject.parseObject(body, User.class);
        System.out.println("一次确认"+o);
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        final String rocketmq_keys =String.valueOf(msg.getHeaders().get("rocketmq_KEYS"));
        System.out.println("checkKey"+rocketmq_keys);
        final Object payload = msg.getPayload();
        final User o = JSONObject.parseObject((byte[]) payload, User.class);
        System.out.println("checkDTO:"+o);
        System.out.println("checkLocalTransaction"+msg);
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}