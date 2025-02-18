/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : psychological

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 18/02/2025 17:02:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for assessments
-- ----------------------------
DROP TABLE IF EXISTS `assessments`;
CREATE TABLE `assessments`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `questionnaire` json NOT NULL,
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of assessments
-- ----------------------------
INSERT INTO `assessments` VALUES (1, '抑郁自评量表', 'PHQ-9抑郁症筛查量表', '{\"questions\": [{\"id\": 1, \"text\": \"做事时提不起劲或没有兴趣\", \"options\": [{\"id\": 1, \"text\": \"完全不会\", \"score\": 0}, {\"id\": 2, \"text\": \"几天\", \"score\": 1}, {\"id\": 3, \"text\": \"一半以上天数\", \"score\": 2}, {\"id\": 4, \"text\": \"几乎每天\", \"score\": 3}]}, {\"id\": 2, \"text\": \"感到心情低落、沮丧或绝望\", \"options\": [{\"id\": 5, \"text\": \"完全不会\", \"score\": 0}, {\"id\": 6, \"text\": \"几天\", \"score\": 1}, {\"id\": 7, \"text\": \"一半以上天数\", \"score\": 2}, {\"id\": 8, \"text\": \"几乎每天\", \"score\": 3}]}]}', '1.1', '2025-02-13 15:21:53', 1);
INSERT INTO `assessments` VALUES (3, '焦虑自评量表(GAD-7)', '在过去的两周里，有多少时候您受到以下问题的困扰？', '\"\\\"{\\\\\\\"questions\\\\\\\": [{\\\\\\\"id\\\\\\\": 1, \\\\\\\"text\\\\\\\": \\\\\\\"感到紧张、焦虑或心烦意乱\\\\\\\", \\\\\\\"options\\\\\\\": [{\\\\\\\"id\\\\\\\": 1, \\\\\\\"text\\\\\\\": \\\\\\\"完全没有\\\\\\\", \\\\\\\"score\\\\\\\": 0}, {\\\\\\\"id\\\\\\\": 2, \\\\\\\"text\\\\\\\": \\\\\\\"几天\\\\\\\", \\\\\\\"score\\\\\\\": 1}, {\\\\\\\"id\\\\\\\": 3, \\\\\\\"text\\\\\\\": \\\\\\\"超过一周\\\\\\\", \\\\\\\"score\\\\\\\": 2}, {\\\\\\\"id\\\\\\\": 4, \\\\\\\"text\\\\\\\": \\\\\\\"几乎每天\\\\\\\", \\\\\\\"score\\\\\\\": 3}]}, {\\\\\\\"id\\\\\\\": 2, \\\\\\\"text\\\\\\\": \\\\\\\"无法停止或控制担忧\\\\\\\", \\\\\\\"options\\\\\\\": [{\\\\\\\"id\\\\\\\": 1, \\\\\\\"text\\\\\\\": \\\\\\\"完全没有\\\\\\\", \\\\\\\"score\\\\\\\": 0}, {\\\\\\\"id\\\\\\\": 2, \\\\\\\"text\\\\\\\": \\\\\\\"几天\\\\\\\", \\\\\\\"score\\\\\\\": 1}, {\\\\\\\"id\\\\\\\": 3, \\\\\\\"text\\\\\\\": \\\\\\\"超过一周\\\\\\\", \\\\\\\"score\\\\\\\": 2}, {\\\\\\\"id\\\\\\\": 4, \\\\\\\"text\\\\\\\": \\\\\\\"几乎每天\\\\\\\", \\\\\\\"score\\\\\\\": 3}]}, {\\\\\\\"id\\\\\\\": 3, \\\\\\\"text\\\\\\\": \\\\\\\"过分担心不同的事情\\\\\\\", \\\\\\\"options\\\\\\\": [{\\\\\\\"id\\\\\\\": 1, \\\\\\\"text\\\\\\\": \\\\\\\"完全没有\\\\\\\", \\\\\\\"score\\\\\\\": 0}, {\\\\\\\"id\\\\\\\": 2, \\\\\\\"text\\\\\\\": \\\\\\\"几天\\\\\\\", \\\\\\\"score\\\\\\\": 1}, {\\\\\\\"id\\\\\\\": 3, \\\\\\\"text\\\\\\\": \\\\\\\"超过一周\\\\\\\", \\\\\\\"score\\\\\\\": 2}, {\\\\\\\"id\\\\\\\": 4, \\\\\\\"text\\\\\\\": \\\\\\\"几乎每天\\\\\\\", \\\\\\\"score\\\\\\\": 3}]}]}\\\"\"', '1.0', '2025-02-14 13:49:07', 1);
INSERT INTO `assessments` VALUES (5, '测试', '测试', '{\"questions\": [{\"id\": 1, \"text\": \"测试\", \"options\": [{\"id\": 1, \"text\": \"完全没有\", \"score\": 0}, {\"id\": 2, \"text\": \"几天\", \"score\": 1}, {\"id\": 3, \"text\": \"超过一周\", \"score\": 2}, {\"id\": 4, \"text\": \"几乎每天\", \"score\": 3}]}]}', '1', '2025-02-18 14:53:28', 1);
INSERT INTO `assessments` VALUES (6, '测试223', '测试', '{\"questions\": [{\"id\": 1, \"text\": \"测试\", \"options\": [{\"id\": 1, \"text\": \"完全没有1\", \"score\": 0}, {\"id\": 2, \"text\": \"几天\", \"score\": 1}, {\"id\": 3, \"text\": \"超过一周\", \"score\": 2}, {\"id\": 4, \"text\": \"几乎每天\", \"score\": 3}]}]}', '1', '2025-02-18 14:54:20', 1);

-- ----------------------------
-- Table structure for psychological_records
-- ----------------------------
DROP TABLE IF EXISTS `psychological_records`;
CREATE TABLE `psychological_records`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `gender` enum('MALE','FEMALE','OTHER') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `age` int NOT NULL,
  `occupation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stress_level` enum('LOW','MEDIUM','HIGH') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sleep_quality` enum('GOOD','NORMAL','POOR') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `emotional_state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `last_assessment_date` datetime NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `psychological_records_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of psychological_records
-- ----------------------------
INSERT INTO `psychological_records` VALUES (1, 8, '张三', 'MALE', 25, '工程师', 'LOW', 'NORMAL', '平稳', '工作压力适中', NULL, '2025-02-14 10:17:44', NULL, 0);

-- ----------------------------
-- Table structure for result_explanations
-- ----------------------------
DROP TABLE IF EXISTS `result_explanations`;
CREATE TABLE `result_explanations`  (
  `assessment_id` int NOT NULL,
  `min_score` int NOT NULL,
  `max_score` int NOT NULL,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `recommendation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`assessment_id`, `category`) USING BTREE,
  CONSTRAINT `result_explanations_ibfk_1` FOREIGN KEY (`assessment_id`) REFERENCES `assessments` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of result_explanations
-- ----------------------------
INSERT INTO `result_explanations` VALUES (1, 10, 14, '中度抑郁', '存在明显抑郁症状', '建议寻求专业帮助');
INSERT INTO `result_explanations` VALUES (1, 0, 4, '无显著抑郁', '情绪状态良好', '保持健康生活习惯');
INSERT INTO `result_explanations` VALUES (1, 5, 9, '轻度抑郁', '可能有轻度抑郁症状', '建议自我调节并观察');
INSERT INTO `result_explanations` VALUES (1, 15, 27, '重度抑郁', '需要立即专业干预', '请立即联系心理咨询师');
INSERT INTO `result_explanations` VALUES (3, 5, 10, '一般', '心理状态一般22', '需要调整自身的心态');
INSERT INTO `result_explanations` VALUES (3, 0, 4, '正常', '心理状态正常', '保持良好的心态');
INSERT INTO `result_explanations` VALUES (5, 0, 3, '正常2', '是个正常人', '没啥建议');

-- ----------------------------
-- Table structure for user_assessments
-- ----------------------------
DROP TABLE IF EXISTS `user_assessments`;
CREATE TABLE `user_assessments`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `assessment_id` int NOT NULL,
  `answers` json NOT NULL,
  `total_score` int NOT NULL,
  `result_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `assessment_id`(`assessment_id` ASC) USING BTREE,
  CONSTRAINT `user_assessments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_assessments_ibfk_2` FOREIGN KEY (`assessment_id`) REFERENCES `assessments` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_assessments
-- ----------------------------
INSERT INTO `user_assessments` VALUES (1, 8, 1, '[{\"optionId\": 2, \"questionId\": 1}, {\"optionId\": 6, \"questionId\": 2}]', 2, '正常', '2025-02-14 10:26:26');
INSERT INTO `user_assessments` VALUES (2, 8, 1, '[{\"optionId\": 2, \"questionId\": 1}, {\"optionId\": 6, \"questionId\": 2}]', 2, '正常', '2025-02-14 10:28:45');
INSERT INTO `user_assessments` VALUES (3, 8, 1, '[{\"optionId\": 2, \"questionId\": 1}, {\"optionId\": 6, \"questionId\": 2}]', 2, '无显著抑郁', '2025-02-14 10:40:08');
INSERT INTO `user_assessments` VALUES (6, 8, 3, '[{\"optionId\": 4, \"questionId\": 1}, {\"optionId\": 4, \"questionId\": 2}, {\"optionId\": 4, \"questionId\": 3}]', 9, '一般', '2025-02-14 14:06:51');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/default-avatar.png',
  `role` enum('USER','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'USER',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  `deleted_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username_deleted`(`username` ASC, `is_deleted` ASC) USING BTREE,
  INDEX `idx_email_deleted`(`email` ASC, `is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'testuser1', '$2a$10$ExampleHash', 'user@example.com', '/avatars/1.jpg', 'USER', '2025-02-13 15:21:53', 0, NULL);
INSERT INTO `users` VALUES (2, 'admin', '$2a$10$hGlm.prF1FZJU2pgbfGoJOKtVIvEzAxe.4YyAjCcPbkSx//7xZOu2', 'admin@example.com', '/default-avatar.png', 'ADMIN', '2025-02-13 15:21:53', 0, NULL);
INSERT INTO `users` VALUES (3, 'testuser2', '$2a$10$hGlm.prF1FZJU2pgbfGoJOKtVIvEzAxe.4YyAjCcPbkSx//7xZOu2', 'test@example.com', '/default-avatar.png', 'USER', NULL, 0, NULL);
INSERT INTO `users` VALUES (8, 'testuser3', '$2a$10$vNAutDpEcfHVw51/E4fbWul2PdQL53Snv6SZCfY8a1GLdSMmqehvW', 'test2@example.com', '/default-avatar.png', 'USER', '2025-02-13 16:48:25', 0, NULL);
INSERT INTO `users` VALUES (10, 'testuser6', '$2a$10$u0o6.Yk1MJtJzEjpWQGv0u7rncFre9upDAGTzJKhFyk692/KUcBhG', '123@qq.com', '/default-avatar.png', 'USER', '2025-02-14 09:32:06', 0, NULL);
INSERT INTO `users` VALUES (11, 'updateduser', '$2a$10$qMkMGz9tn6HErMkOrKIspO1dEoh0ThCR8vq/ljZ4L3bihD6Oo1oW2', 'newemail@example.com', '/avatars/new.png', 'USER', '2025-02-14 11:27:26', 1, NULL);
INSERT INTO `users` VALUES (17, 'yhl232', '$2a$10$MeIMXB2y/AAxPEgy7Yvhl.QbIrNGVR9hja5lpbvFIOW2Cgb/qMg6G', '22@qq.com', '/default-avatar.png', 'USER', '2025-02-18 10:11:50', 0, NULL);
INSERT INTO `users` VALUES (19, 'yhl2', '$2a$10$axxZBQOj8g4irGOxtrgSx.rYpjiP902spzZlBgrAMN3h7oSBeAfqK', '2@qq.com', '/default-avatar.png', 'USER', '2025-02-18 10:29:52', 1, NULL);
INSERT INTO `users` VALUES (20, 'wjt1', '$2a$10$JRvJz/CThnDWXT/lGra/aeq26FeSd1ViC4ojRYpHWwfs41W8b2Pn6', 'wjt@qq.com', '/default-avatar.png', 'USER', '2025-02-18 10:47:11', 0, NULL);
INSERT INTO `users` VALUES (21, 'testuser4', '$2a$10$uTBeWdwSZNzzqkQVlQwuDeSmsiLh/ozNfOj9BHTfSeC6dE14mshfO', 'testuser4@qq.com', '/default-avatar.png', 'USER', '2025-02-18 13:51:32', 0, NULL);
INSERT INTO `users` VALUES (22, 'testuser5', '$2a$10$dQDIoDky1Zis8oCFUteQwueW8voGjUXcaZ7mwDfz1Ccj1o9vgXfoC', 'testuser5@qq.com', '/default-avatar.png', 'USER', '2025-02-18 13:51:43', 0, NULL);
INSERT INTO `users` VALUES (23, 'testuser7@qq.com', '$2a$10$BMjL4fykPgwn9K21aEvveuLEQ8Efbhm2uVQUKC.nA.vROBq.koFTK', 'testuser7@qq.com', '/default-avatar.png', 'USER', '2025-02-18 13:52:08', 0, NULL);
INSERT INTO `users` VALUES (24, 'testuser8', '$2a$10$sQ2XN3II.V1HuRLk7OsToeJybtWpZ3Ae9Uxf7UjNgWAxcvdKK8hIq', 'testuser8@qq.com', '/default-avatar.png', 'USER', '2025-02-18 13:53:03', 0, NULL);

SET FOREIGN_KEY_CHECKS = 1;
