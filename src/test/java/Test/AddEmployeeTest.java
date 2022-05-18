package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.CommonMethods;
import utils.ConfigReader;
import utils.Constants;
import utils.ExcelReader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddEmployeeTest extends CommonMethods {
//    read the configuration file for username and password
//    and add an employee
    @Test
    public void addEmployee(){

        login.LoginMethod(ConfigReader.getPropertyValue("username"),
                ConfigReader.getPropertyValue("password"));

        click(dash.pimOption);
        click(dash.addEmployeeButton);

      sendText(addEmployeePage.firstNameField,"teyfur");
      sendText(addEmployeePage.middleNameField,"voich");
      sendText(addEmployeePage.lastNameField,"ddrru");

//get the employee id
        String empID = addEmployeePage.empIDLocator.getAttribute("value");
        click(addEmployeePage.saveButton);

        System.out.println(empID);

        click(dash.pimOption);
        click(dash.employeeListOption);

        sendText(employeeSearchPage.idField,empID);
        click(employeeSearchPage.searchButton);

        List<WebElement> rowData = employeeSearchPage.rowData;
        for(WebElement data : rowData){
           String ActualId=data.getText();
            Assert.assertEquals(empID,ActualId);
        }
    }

    @Test
    public void addMultipleEmployee() throws InterruptedException {
//read the employee data from excel file
//assert that u have succesfully added the employee

        login.LoginMethod(ConfigReader.getPropertyValue("username"),
                ConfigReader.getPropertyValue("password"));

        click(dash.pimOption);
        click(dash.addEmployeeButton);
        List<Map<String,String>> newEmployees = ExcelReader.excelIntoMap(Constants.TESTDATA_FILEPATH,"EmployeeData");
        Iterator<Map<String,String>> itr = newEmployees.iterator();
        while(itr.hasNext()){
            Map<String,String> mapNewEmp = itr.next();
            sendText(addEmployeePage.firstNameField, mapNewEmp.get("FirstName"));
            sendText(addEmployeePage.middleNameField, mapNewEmp.get("MiddleName"));
            sendText(addEmployeePage.lastNameField, mapNewEmp.get("LastName"));
            String empIdValue = addEmployeePage.empIDLocator.getAttribute("value");

            click(addEmployeePage.saveButton);

            Thread.sleep(3000);
            click(employeeSearchPage.empListOption);
            sendText(employeeSearchPage.idField, empIdValue);
            click(employeeSearchPage.searchButton);
            SoftAssert softAssert = new SoftAssert();
            List<WebElement> rowData = driver.findElements(By.xpath("//*[@id='resultTable']/tbody/tr"));
            for (int i = 0; i < rowData.size(); i++) {
                String rowText = rowData.get(i).getText();
                System.out.println(rowText);
                String expectedData = empIdValue+" "+mapNewEmp.get("FirstName")+" "+mapNewEmp.get("MiddleName")+" "+mapNewEmp.get("LastName");
                Assert.assertEquals(expectedData,rowText);
//                softAssert.assertEquals(expectedData,rowText);
//                softAssert.assertAll();
            }
//            List<WebElement> rowData = addEmployeePage.rowData;
//            for (WebElement data : rowData) {
//                String actualID = data.getText();
//                Assert.assertEquals(actualID, empIdValue);
//            }
            click(dash.addEmployeeButton);
        }
    }
}
