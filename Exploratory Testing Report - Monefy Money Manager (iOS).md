# Exploratory Testing Report - Monefy Money Manager (iOS)

**Date:** 2026-02-02
**Platform:** iOS
**Application:** Monefy - Money Manager
**Testing Type:** Exploratory & Risk-based Manual Testing
**Tester:** Ashna Mekkayil

-----

## 1.Testing Objective

The objective of this exploratory testing session was to evaluate **data accuracy, financial integrity, usability, and stability** of the Monefy iOS application with a strong focus
on **risk-prone and user-critical flows**.Special attention was given to areas that directly impact ** user trust and data protection**, such as transaction handling, 
budgets, recurring entries, synchronization, and security. The goal was to proactively identify gaps that could lead to **incorrect financial data, loss of user confidence, 
or potential security and compliance risks**

----

##2.Exploratory Testing Charters(Prioritized)

| Charter ID | Priority  | Objective                           | Focus area           |
| ------------ --------   -------------------------------------  --------------------
|C1          |**High**   |Verify add/delete transactions       | Core transactions    |
|C5          |**High**   |Test sync & cloud backup             |Cloud sync & Backup   |
|C6          |**High**   |Validate security features           |Security & Privacy    |
|C2          |**High**   |Validate multi-currency & categories |Account & Categories  |
|C3          |**Medium** |Explore budget creation & reports    |Budgets & Periods     |
|C4          |**Medium** |Validate recurring transactions      |Recurring Transactions|
|C7          |**Medium** |UI behaviour & performance           |UI & Performance      |
|C8          |**Medium** |Test subscription flows              |Subscription          |
|C9          |**Medium** |Stress & error handling              |Edge cases            |

##3.Findings by Charter

### C1 - Core Transactions

**Observations:**

* Adding, editing, and deleting transactions works as expected.
* Decimal values auto-format correctly(e.g,'10.1 -> 10.10').
* Transaction timestamps updates accurately.

**Findings Observed During Testing:**

* Arithmetic expressions(e.g, '9-3') behave inconsistently depending on entry path.
* Negative values handled differently via quick-add vs manual entry.

**Impact:** Incorrect balance calculation impacting financial accuracy.

----

### C2 - Accounts & Categories

**Observations:**

* Category selection and editing works for predefined categories.
* Multi-currency symbol can be changed.

**Findings Observed During Testing:**

* Currency conversion not applied when switching currencies (e.g, '950 €' remains '950 INR').
* Custom category names cannot be saved.
* Deleting categories does not immediately refresh reports.
* Long-press on categories causes UI text overlap.

----

### C3 - Budgets & Periods

**Observations:**

* Monthly budgets calculate correctly for included transactions.

**Findings Observed During Testing:**

* **Overspending alerts do not appear** even when expenses exceed the configured budget.
* Budget input retains old values after removal and re-creation.
* Reports do not refresh when adding back-dated transactions.

**Impact:** Users are not notified when exceeding budget limits, reducing trust in financial tracking.

----

### C4 - Recurring Transactions
 
**Observations:**

* Recurring transactions are created correctly.
* Frequency updates apply to future instances.

**Findings Observed During Testing:**

* Deleting a recurring series does not immediately remove all future entries.

----

### C5 - Cloud sync & Backup
 
**Observations:**

* iCloud/Google Drive backup completes successfully.
* Sync works across devices.

**Findings Observed During Testing:**

* No clear conflict resolution during simultaneous edits; silent overwrite observed.

----

### C6 - Security & Privacy
 
**Observations:**

* Passcode and biometric authentication work as expected.

**Findings Observed During Testing:**

* App does not consistently auto-lock after 30 seconds in background.

----

### C7 - UI & Performance
 
**Observations:**

* Smooth performance with small datasets.
* Dark/Light mode supported.

**Findings Observed During Testing:**

* Lag observed when opening charts with >500transactions.
* Year view adds transactions with current date onstead of selected year context.

----

### C8 - Subscription & Purchase
 
**Observations:**

* Purchase and restore flows work correctly.

**Findings Observed During Testing:**

* Cancelled trial does not immediately revoke premium features.

----

### C9 - Stress & Error Handling
 
**Observations:**

* App handles offline mode gracefully.

**Findings Observed During Testing:**

* Amount '0' allowed as a valid transaction.
* Very large numbers and arithmetic expressions behave inconsistently.

----

## 4. Critical User Flow Testing

### CF1 - Add Expense/Income(Highest risk)

**Flow:** Home->Add->Enter amount->Select category->Save

**Edge Cases Tested:**

* Amount '0', negative values, large numbers
* Rapid Save taps (duplicate prevention)
* Switching income/expense mid-entry
* Copy-paste currency values

**Result:** Functional gaps found in validation and duplicate handling.

----

### CF2 - Balance Recalculation

* Editing or deleting past transactions recalculates balances correctly.
* Issues observed when editing transactions across budget periods.

----
### CF3 - Multi-Currency Handling

* Currency change after transactions exist does not convert historical data.
* Reports show symbol change without value normalization.

----
### CF4 - Recurring transactions Execution

* Monthly recurrences on 29/30/31 show inconsistent behaviour.
* Deleting a single instance vs entire series not clearly differentiated.

----

### CF5 - Sync, Restore & Data Integrity

* Sync across two devices works under normal conditions.
* Network interruption during sync risks partial updates.

----

## 5. Additional Edge Cases Covered

### Input & Validation

* Leading/trailing spaces in amount fields
* Unicode characters in notes
* Long notes(>500 chars)

### UI/Accessibility

* Large text (Accessibility settings) 
* Orientation change during transaction entry

### Performance

* App with large transaction history
* Cold start after long inactivity

### Security

* App Backgrounded during transaction entry
* Biometric change on device

----

## 6.Risk Assessment and Prioritization

|        Area             |       Risk      |       Reason              |
|------------------------ |-----------------|---------------------------|
|Transactions and Balance |Critical         |Direct Financial impact    |
|Sync and Backup          |Critical         |Data loss risk             |
|Currency Handling        |High             |Misleading reports         |
|Recurring transactions   |High             |Duplicate/Missing entry    |
|Budgets                  |Medium           |Reporting inconsistencies  |
|Subscription             |Medium           |Entitlement issues         |

----

## 7. Conclusion

The Monefy iOS application is **Functionally stable** for standard usage but exhibits **critical gaps in data validation, currency handling, and sync conflict management**. 
These issues pose risks to **financial accuracy and use trust**.

This exploratory session focused on realistic user behaviour and uncovered functional gaps, edge-case inconsistencies, and UX limitations that may not be detected through 
scripted testing alone. Addressing the observations noted above would improve overall data accuracy, user confidence, and application reliability.
