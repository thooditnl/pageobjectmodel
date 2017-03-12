package com.seleniumpom.tests.mappings;

/**
 * Created by THOODI on 3/11/2017.
 */
public class LoginMapping extends BaseMapping {

    private static LoginMapping instance;
    private LoginMapping()
    {
    }

    public static LoginMapping getInstance()
    {
        if (instance == null)
        {
            instance = new LoginMapping();
        }
        return instance;
    }
}
