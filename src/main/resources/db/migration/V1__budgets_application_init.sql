CREATE TABLE budget_categories
(
    id               VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    user_id          VARCHAR(255) NOT NULL,
    name             VARCHAR(30)  NOT NULL,
    notes            VARCHAR(100),
    budget_type      VARCHAR(255) NOT NULL,
    is_active        BOOLEAN,
    CONSTRAINT pk_budget_categories PRIMARY KEY (id)
);

CREATE TABLE budgets
(
    id                  VARCHAR(255)   NOT NULL,
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    last_modified_at    TIMESTAMP WITHOUT TIME ZONE,
    created_by          VARCHAR(255),
    last_modified_by    VARCHAR(255),
    user_id             VARCHAR(255)   NOT NULL,
    budget_category_id  VARCHAR(255)   NOT NULL,
    amount              DECIMAL(15, 2) NOT NULL,
    name                VARCHAR(50)    NOT NULL,
    budget_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    receipt_url         VARCHAR(255),
    tags                TEXT[],
    is_recurring        BOOLEAN,
    recurring_budget_id VARCHAR(255),
    version             BIGINT,
    CONSTRAINT pk_budgets PRIMARY KEY (id)
);

CREATE TABLE payment_reminders
(
    id               VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    user_id          VARCHAR(255) NOT NULL,
    category_id      VARCHAR(255) NOT NULL,
    reminder_name    VARCHAR(255) NOT NULL,
    frequency        VARCHAR(255) NOT NULL,
    next_due_date    date         NOT NULL,
    status           VARCHAR(255) NOT NULL,
    CONSTRAINT pk_payment_reminders PRIMARY KEY (id)
);

CREATE TABLE recurring_budgets
(
    id                 VARCHAR(255)   NOT NULL,
    created_at         TIMESTAMP WITHOUT TIME ZONE,
    last_modified_at   TIMESTAMP WITHOUT TIME ZONE,
    created_by         VARCHAR(255),
    last_modified_by   VARCHAR(255),
    user_id            VARCHAR(255)   NOT NULL,
    budget_category_id VARCHAR(255)   NOT NULL,
    amount             DECIMAL(15, 2) NOT NULL,
    name               VARCHAR(255),
    description        VARCHAR(255),
    frequency          VARCHAR(255)   NOT NULL,
    frequency_interval INTEGER,
    start_date         date           NOT NULL,
    end_date           date,
    next_occurrence    date,
    is_active          BOOLEAN,
    CONSTRAINT pk_recurring_budgets PRIMARY KEY (id)
);

CREATE TABLE users
(
    id               VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    email            VARCHAR(255) NOT NULL,
    username         VARCHAR(255) NOT NULL,
    password_hash    VARCHAR(255),
    is_active        BOOLEAN      NOT NULL,
    account_locked   BOOLEAN,
    roles            TEXT[],
    provider_id      VARCHAR(255),
    provider_type    VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

-- UNIQUE CONSTRAINTS
ALTER TABLE payment_reminders
    ADD CONSTRAINT uc_1acee4f8e4f835ae78fb71e8d UNIQUE (user_id, reminder_name);

ALTER TABLE budget_categories
    ADD CONSTRAINT uc_80ba44bb8f141599f689cd938 UNIQUE (user_id, name);

ALTER TABLE budgets
    ADD CONSTRAINT uc_f161226773c0302184cf8c6c5 UNIQUE (recurring_budget_id, budget_date);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

-- DEFAULTS
ALTER TABLE budgets
    ALTER COLUMN is_recurring SET DEFAULT false;
ALTER TABLE budget_categories
    ALTER COLUMN is_active SET DEFAULT true;
ALTER TABLE recurring_budgets
    ALTER COLUMN is_active SET DEFAULT true;
ALTER TABLE payment_reminders
    ALTER COLUMN status SET DEFAULT 'ACTIVE';
ALTER TABLE budgets
    ALTER COLUMN version SET DEFAULT 0;


-- INDICES
CREATE INDEX idx_provider_id_provider_type ON users (provider_id, provider_type);

CREATE INDEX idx_user_budget_date ON budgets (user_id, budget_date);

CREATE INDEX idx_user_next_due_date ON payment_reminders (user_id, next_due_date);

ALTER TABLE budgets
    ADD CONSTRAINT FK_BUDGETS_ON_BUDGET_CATEGORY FOREIGN KEY (budget_category_id) REFERENCES budget_categories (id);

ALTER TABLE budgets
    ADD CONSTRAINT FK_BUDGETS_ON_RECURRING_BUDGET FOREIGN KEY (recurring_budget_id) REFERENCES recurring_budgets (id);

CREATE INDEX idx_recurring_budget_id ON budgets (recurring_budget_id);

ALTER TABLE budgets
    ADD CONSTRAINT FK_BUDGETS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE budget_categories
    ADD CONSTRAINT FK_BUDGET_CATEGORIES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE payment_reminders
    ADD CONSTRAINT FK_PAYMENT_REMINDERS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES budget_categories (id);

ALTER TABLE payment_reminders
    ADD CONSTRAINT FK_PAYMENT_REMINDERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE INDEX idx_user_id ON payment_reminders (user_id);

ALTER TABLE recurring_budgets
    ADD CONSTRAINT FK_RECURRING_BUDGETS_ON_BUDGET_CATEGORY FOREIGN KEY (budget_category_id) REFERENCES budget_categories (id);

ALTER TABLE recurring_budgets
    ADD CONSTRAINT FK_RECURRING_BUDGETS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);