-- Tenant Entity Indexes
CREATE INDEX idx_tenant_id ON tenant(id);
CREATE INDEX idx_tenant_name ON tenant(name);

-- User Entity Indexes
CREATE INDEX idx_user_id ON user(id);
CREATE INDEX idx_user_username ON user(username);

-- Project Entity Indexes
CREATE INDEX idx_project_name ON project(name);
CREATE INDEX idx_project_status ON project(status);

-- Notification Entity Indexes
CREATE INDEX idx_notification_user_id ON notification(user_id);
CREATE INDEX idx_notification_status ON notification(status);

-- Comment Entity Indexes
CREATE INDEX idx_comment_user_id ON comment(user_id);
CREATE INDEX idx_comment_project_id ON comment(project_id);