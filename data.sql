/* 
满足心理健康评估系统的基本要求，包括用户注册登录功能、心理测评问卷的填写与提交、测评结果的自动分析与展示、个人心理健康档案管理、数据统计分析功能
 */
-- 创建数据库
CREATE DATABASE IF NOT EXISTS psychological DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE psychological;

-- 用户表（核心身份信息）
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,  -- 前端传输需加密，后端再哈希
    email VARCHAR(100) UNIQUE NOT NULL,
    avatar VARCHAR(255) DEFAULT '/default-avatar.png',  -- 头像存储路径
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_deleted TINYINT(1) DEFAULT 0
) ENGINE=InnoDB;

-- 心理档案表
CREATE TABLE psychological_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,                          -- 真实姓名
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,   -- 性别
    age INT NOT NULL,                                  -- 年龄
    occupation VARCHAR(50),                            -- 职业
    stress_level ENUM('LOW', 'MEDIUM', 'HIGH'),       -- 压力水平
    sleep_quality ENUM('GOOD', 'NORMAL', 'POOR'),     -- 睡眠质量
    emotional_state VARCHAR(50),                       -- 情绪状态
    notes TEXT,                                        -- 备注信息
    last_assessment_date DATETIME,                     -- 最近评估日期
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,     -- 创建时间
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
    is_deleted TINYINT(1) DEFAULT 0,                  -- 逻辑删除标志
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id)                       -- 添加索引提高查询性能
) ENGINE=InnoDB;

-- 心理测评问卷表
CREATE TABLE assessments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    questionnaire JSON NOT NULL,  -- 包含问题、选项和评分规则
    version VARCHAR(20) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_active TINYINT(1) DEFAULT 1
) ENGINE=InnoDB;

-- 用户测评记录表
CREATE TABLE user_assessments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    assessment_id INT NOT NULL,
    answers JSON NOT NULL,        -- 存储用户选择的选项ID和得分
    total_score INT NOT NULL,
    result_category VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (assessment_id) REFERENCES assessments(id)
) ENGINE=InnoDB;

-- 结果解释表（评分规则）
CREATE TABLE result_explanations (
    assessment_id INT NOT NULL,
    min_score INT NOT NULL,
    max_score INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    recommendation TEXT NOT NULL,
    PRIMARY KEY (assessment_id, category),
    FOREIGN KEY (assessment_id) REFERENCES assessments(id)
) ENGINE=InnoDB;

-- 示例测评问卷
INSERT INTO assessments (title, description, questionnaire, version)
VALUES (
    '抑郁自评量表', 
    'PHQ-9抑郁症筛查量表',
    '{
        "questions": [
            {
                "id": 1,
                "text": "做事时提不起劲或没有兴趣",
                "options": [
                    {"id": 1, "score": 0, "text": "完全不会"},
                    {"id": 2, "score": 1, "text": "几天"},
                    {"id": 3, "score": 2, "text": "一半以上天数"},
                    {"id": 4, "score": 3, "text": "几乎每天"}
                ]
            },
            {
                "id": 2,
                "text": "感到心情低落、沮丧或绝望",
                "options": [
                    {"id": 5, "score": 0, "text": "完全不会"},
                    {"id": 6, "score": 1, "text": "几天"},
                    {"id": 7, "score": 2, "text": "一半以上天数"},
                    {"id": 8, "score": 3, "text": "几乎每天"}
                ]
            }
        ]
    }',
    '1.1'
);

