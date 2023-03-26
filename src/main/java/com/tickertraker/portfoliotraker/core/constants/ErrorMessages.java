package com.tickertraker.portfoliotraker.core.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {
    public final static String ERROR_PORTFOLIO_CREATION = "Error while creating portfolio. Try again Later!";
    public final static String ERROR_PORTFOLIO_DELETION = "Error while deleting portfolio. Try again Later!";
    public final static String ERROR_PORTFOLIO_RENAME = "Error while renaming portfolio. Try again Later!";
    public final static String ERROR_PORTFOLIO_CREATION_DUPLICATE = "Error while creating portfolio. Portfolio Name Exists. Choose a different Name";
}
