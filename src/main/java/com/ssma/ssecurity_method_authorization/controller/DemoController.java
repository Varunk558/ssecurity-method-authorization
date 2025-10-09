package com.ssma.ssecurity_method_authorization.controller;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @GetMapping("/demo1")
    @PreAuthorize("hasAuthority('read')")  // hasAuthority(), hasAnyAuthority(), hasRole(), hasAnyRole()
    public String demo(){

        return "demo1";
    }

    @GetMapping("/demo2")
    @PreAuthorize("hasAnyAuthority('write', 'read')")
    public String demo2(){

        return "demo2";
    }

    @GetMapping("/demo3/{smth}")
    @PreAuthorize(
                    """
                    (#something == authentication.name) or
                    hasAnyAuthority('write', 'read')
                    """)
    public String demo3(@PathVariable("smth") String something){

        return "Demo 3";
    }

    @GetMapping("/demo4/{smth}")
    @PreAuthorize("@demo4ConditionEvaluator.condition()")
    public String demo4(){

        return "Demo 4";
    }

    //  @PostAuthorize

    @GetMapping("/demo5")
    @PostAuthorize("returnObject=='Demo 5'")  // is mainly used when we want to restrict the access to some returned value
    public String demo5(){

        System.out.println(":)");  // never use @PostAuthorize with methods that change data
        return "Demo 5";
    }

    // @PreFilter   ==> works with either array or Collection
    @GetMapping("/demo6")
    @PreFilter("filterObject.contains('a')")
    public String demo6(@RequestBody List<String> values){
        System.out.println("Values: " +values);
        return "Demo 6";
    }

    // @PostFilter ==> the returned type must be either a Collection or an Array

    @GetMapping("/demo7")
    @PostFilter("filterObject.contains('a')")
    public List<String> demo7(){

        return List.of("abcd","bcd","qaaz","wrty");  // List.of created an immutable collection, always the request should fail give an exception but,  surprisingly it worked
    }
}
