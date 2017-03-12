package com.seleniumpom.tests.framework;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class LoggingListener extends TestListenerAdapter  implements IInvokedMethodListener {

  public void onTestStart(ITestResult result) {
    System.out.println("**** " + getTestName(result) + " START");
  }

  public void onTestSuccess(ITestResult result) {
    System.out.println("**** " + getTestName(result) + " PASSED");
  }

  public void onTestFailure(ITestResult result) {
    System.out.println("**** " + getTestName(result) + " FAILED");
  }
  public void onTestSkipped(ITestResult result) {
    System.out.println("**** " + getTestName(result) + " SKIPPED");
  }


  private String getTestName(ITestResult result){
    return result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
  }


  public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

  }

  public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

  }
}
