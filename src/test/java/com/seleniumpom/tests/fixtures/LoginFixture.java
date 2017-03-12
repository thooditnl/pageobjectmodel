package com.seleniumpom.tests.fixtures;

import com.seleniumpom.tests.framework.WebDriverWrapper;
import com.seleniumpom.tests.mappings.LoginMapping;

/**
 * Created by THOODI on 3/11/2017.
 */
public class LoginFixture extends BaseFixture {
    private LoginMapping map;
    private WebDriverWrapper selenium;

    public LoginFixture(WebDriverWrapper selenium, LoginMapping map)
    {
        super(selenium, map);
        this.map = map;
        this.selenium = selenium;
    }
}
