-- Passages
INSERT INTO passage (id, title, part_type, skill_type, body, audio_script) VALUES
(1, 'Part5 sample', 'PART5', 'READING', '', NULL),
(2, 'Part6 sample', 'PART6', 'READING', 'Dear team,

Please review the attached report [1] provide feedback by end of day. The report should be [2] before the meeting.

Best,
Manager', NULL),
(3, 'Part7 sample', 'PART7', 'READING', 'Announcement:
Office will be closed next Monday for maintenance.', NULL);

-- Questions
INSERT INTO question (id, passage_id, question_text, blank_number, explanation) VALUES
(1, 1, 'The report ___ by Friday.', NULL, 'Future passive.'),
(2, 2, '空欄(1)に入る最も適切な語を選びなさい。', 1, 'Connective phrase.'),
(3, 2, '空欄(2)に入る最も適切な語を選びなさい。', 2, 'Timing.'),
(4, 3, 'When is the office closed?', NULL, 'Reading comprehension.'),
(5, 3, 'What is the reason for closure?', NULL, 'Reading comprehension.');

-- Choices for question 1 (Part5)
INSERT INTO choice (id, question_id, label, choice_text, is_correct) VALUES
(1, 1, 'A', 'is finishing', FALSE),
(2, 1, 'B', 'is going to complete', FALSE),
(3, 1, 'C', 'will be completed', TRUE),
(4, 1, 'D', 'was completed', FALSE);

-- Choices for question 2 (Part6, blank 1)
INSERT INTO choice (id, question_id, label, choice_text, is_correct) VALUES
(5, 2, 'A', 'and', TRUE),
(6, 2, 'B', 'so', FALSE),
(7, 2, 'C', 'but', FALSE),
(8, 2, 'D', 'or', FALSE);

-- Choices for question 3 (Part6, blank 2)
INSERT INTO choice (id, question_id, label, choice_text, is_correct) VALUES
(9, 3, 'A', 'sent', FALSE),
(10, 3, 'B', 'finalized', TRUE),
(11, 3, 'C', 'delayed', FALSE),
(12, 3, 'D', 'ignored', FALSE);

-- Choices for question 4 (Part7)
INSERT INTO choice (id, question_id, label, choice_text, is_correct) VALUES
(13, 4, 'A', 'This Friday', FALSE),
(14, 4, 'B', 'Next Monday', TRUE),
(15, 4, 'C', 'Tomorrow', FALSE),
(16, 4, 'D', 'Next month', FALSE);

-- Choices for question 5 (Part7)
INSERT INTO choice (id, question_id, label, choice_text, is_correct) VALUES
(17, 5, 'A', 'Holiday', FALSE),
(18, 5, 'B', 'Maintenance', TRUE),
(19, 5, 'C', 'Meeting', FALSE),
(20, 5, 'D', 'Inspection', FALSE);
