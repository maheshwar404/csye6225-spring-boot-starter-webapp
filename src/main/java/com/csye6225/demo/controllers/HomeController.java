package com.csye6225.demo.controllers;


import com.csye6225.demo.dao.UserDao;
import com.csye6225.demo.entity.User;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

  private final static Logger logger = LoggerFactory.getLogger(HomeController.class);
  @Autowired
  private UserDao userDao;
  @Autowired
  private HttpSession session;

  @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String welcome(Model model) throws Exception {
    model.addAttribute("user", new User());
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "you are not logged in. Go to /user/register to register yourself");
    return jsonObject.toString();

  }

  @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String test() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized for /test");
    return jsonObject.toString();
  }

  @RequestMapping(value = "/testPost", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public String testPost() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized for /testPost");
    return jsonObject.toString();
  }


    @RequestMapping(value = "/register.htm", method = RequestMethod.POST)
    protected String registerNewUser(HttpServletRequest request, @ModelAttribute("user") User user,
                                           BindingResult result) throws Exception {

        JsonObject jsonObject = new JsonObject();
        try {

            if (userDao.findUserByEmailId(user.getEmailId()) == null) {
               BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
               String hashedPassword = passwordEncoder.encode(user.getPassword());
               user.setPassword(hashedPassword);
                userDao.save(user);
               // session.invalidate();

                jsonObject.addProperty("message", "You are registered");

                return jsonObject.toString();
            } else {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", "You are not registered");

                return jsonObject.toString();
            }


        } catch (IllegalStateException e) {
            System.out.println("* IllegalStateException: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("* Exception: " + e.getMessage());
        }
            return
    }


}






