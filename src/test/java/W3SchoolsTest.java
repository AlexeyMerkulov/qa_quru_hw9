import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.containExactTextsCaseSensitive;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class W3SchoolsTest extends TestBase {

    @DisplayName("Проверка работы поиска по запросам")
    @ParameterizedTest(name = "Проверка результата поиска для запроса {0}")
    @ValueSource(strings = {"SQL", "JavaScript", "Python"})
    void successfulTopicSearch(String topicName) {
        open("https://www.w3schools.com/");
        $("#search2")
                .setValue(topicName)
                .pressEnter();
        $("#main").shouldHave(text(topicName));
    }

    @DisplayName("Проверка работы навигации по разделам")
    @ParameterizedTest(name = "Проверка результата отображения текста {1} при нажатии на раздел {0}")
    @CsvSource({
            "Tutorials, Tutorials and References",
            "Exercises, Exercises and Quizzes",
            "Certified, Get Certified",
            "Services, All Our Services"
    })
    void successfulNavigationBarWorking(String navBarTopic, String resultHeader) {
        open("https://www.w3schools.com/");
        $(String.format("a[title*='%s']", navBarTopic)).click();
        $(String.format("nav[id*='%s']", navBarTopic.toLowerCase())).shouldHave(text(resultHeader));
    }

    static Stream<Arguments> provideTutorialChapters() {
        return Stream.of(
                Arguments.of(
                        "HTML",
                        List.of("HTML HOME", "HTML Introduction", "HTML Editors", "HTML Basic", "HTML Elements")
                ),
                Arguments.of(
                        "CSS",
                        List.of("CSS HOME", "CSS Introduction", "CSS Syntax", "CSS Selectors", "CSS How To")
                ),
                Arguments.of("SQL",
                        List.of("SQL HOME", "SQL Intro", "SQL Syntax", "SQL Select", "SQL Select Distinct"))
        );
    }

    @DisplayName("Проверка отображения глав туториалов")
    @ParameterizedTest(name = "Проверка отображения глав туториала {0}")
    @MethodSource("provideTutorialChapters")
    void successfulTutorialsChaptersDisplay(String tutorialName, List<String> tutorialChapters) {
        open("https://www.w3schools.com/");
        $(String.format("#topnav a[title='%s Tutorial']", tutorialName)).click();
        $$("#leftmenuinnerinner a[target='_top']").should(containExactTextsCaseSensitive(tutorialChapters));
    }
}
