package com.meesho.notificationservice.controllers;

import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.services.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(NotificationRestController.class)
public class NotificationRestControllerTests {

   @Autowired
   private MockMvc mockMvc;

   @MockBean
   private NotificationService notificationService;


   @Test
   public void findAllNotificationTest_basic() throws Exception{

       when(notificationService.findAll()).thenReturn(Arrays.asList(new Notification("+917869216592", "TEST1"),
                                                                    new Notification("+917862124565", "TEST2")));

       RequestBuilder request = MockMvcRequestBuilders
               .get("/v1/notifications")
               .accept(MediaType.APPLICATION_JSON);

       MvcResult result = mockMvc.perform(request)
               .andExpect(status().isOk())
               .andExpect(content().json("[{phone_number:+917869216592, message:TEST1}, {phone_number:+917862124565, message:TEST2}]"))
               .andReturn();
   }
}
