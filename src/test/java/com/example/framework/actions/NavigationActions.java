package com.example.framework.actions;

import com.example.framework.config.Config;
import com.example.framework.config.NavigationTargetResolver;
import com.example.framework.core.ScenarioContext;
import com.microsoft.playwright.Page;

import java.util.List;

/**
 * Page, tab, and iframe navigation helpers.
 */
public class NavigationActions extends BaseActions {
    private final NavigationTargetResolver targets = NavigationTargetResolver.defaultInstance();

    public NavigationActions(ScenarioContext scenarioContext) {
        super(scenarioContext);
    }

    public void navigate(String url) {
        scenarioContext.page().navigate(resolveValue(url));
    }

    public void navigateToUrlKey(String urlKey) {
        scenarioContext.page().navigate(targets.urlKey(urlKey));
    }

    public void navigateToPage(String pageName) {
        scenarioContext.page().navigate(targets.page(pageName));
    }

    public void refresh() {
        scenarioContext.page().reload();
    }

    public void back() {
        scenarioContext.page().goBack();
    }

    public void forward() {
        scenarioContext.page().goForward();
    }

    public void switchToTab(int index) {
        List<Page> pages = scenarioContext.browserContext().pages();
        int zeroBasedIndex = index <= 0 ? 0 : index - 1;
        if (zeroBasedIndex >= pages.size()) {
            throw new IllegalArgumentException(
                    "No browser tab exists at index " + index + ". Open tabs: " + pages.size());
        }
        switchToPage(pages.get(zeroBasedIndex));
    }

    public void switchToNewestTab() {
        List<Page> pages = scenarioContext.browserContext().pages();
        if (pages.size() < 2) {
            throw new IllegalStateException("No new tab is available. Open tabs: " + pages.size());
        }
        switchToPage(pages.get(pages.size() - 1));
    }

    public void closeCurrentTab() {
        Page currentPage = scenarioContext.page();
        currentPage.close();

        List<Page> remainingPages = scenarioContext.browserContext().pages();
        if (remainingPages.isEmpty()) {
            Page newPage = scenarioContext.browserContext().newPage();
            switchToPage(newPage);
            return;
        }
        switchToPage(remainingPages.get(remainingPages.size() - 1));
    }

    public void switchToIframe(String elementName) {
        scenarioContext.switchToFrame(locator(elementName).contentFrame());
    }

    public void switchToMainContent() {
        scenarioContext.switchToMainContent();
    }

    private void switchToPage(Page page) {
        page.setDefaultTimeout(Config.timeoutMs());
        page.bringToFront();
        scenarioContext.setActivePage(page);
    }
}
