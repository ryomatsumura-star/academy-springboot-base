CREATE TABLE learning_data (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  study_month DATE NOT NULL,
  study_minutes INTEGER NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_learning_data_user
    FOREIGN KEY (user_id)
    REFERENCES users(id),

  CONSTRAINT fk_learning_data_category
    FOREIGN KEY (category_id)
    REFERENCES categories(id)
);