package demowebshop;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class AddToCart {

    @Test
    void ApiAddToCart () {
        step("Получить куки и установить в браузер", () -> {
            String authorizationCookie =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .body("product_attribute_74_5_26=81&" +
                                  "product_attribute_74_6_27=85&" +
                                  "product_attribute_74_3_28=86&" +
                                  "product_attribute_74_8_29=90&" +
                                  "addtocart_74.EnteredQuantity=2")
                            .when()
                            .post("http://demowebshop.tricentis.com/addproducttocart/details/74/1")
                            .then()
                            .statusCode(200)
                            .body("success", is(true))
                            .body("updatetopcartsectionhtml", is("(2)"))
                            .extract()
                            .cookie("Nop.customer");

        step("Открыть небольшой контент для активной сессии, куда можно подставить куки", () ->
            open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/mobile-menu-collapse.png"));

        step("Установить куки в браузер", () ->
            getWebDriver().manage().addCookie(
                    new Cookie("Nop.customer", authorizationCookie)));
                    refresh();
        });

        step("Открыть главную страницу", () -> {
            open("http://demowebshop.tricentis.com/");

        step("Проверить актуальное количество товара в корзине");
            $(".cart-qty").shouldHave(text("2"));
        });

    }

}
