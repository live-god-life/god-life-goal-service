SELECT
    goal_id as id,
    start_date,
    end_date,
    total_mindset_count as m_c,
    total_todo_count as all_t_c,
    completed_todo_count as c_t_c,
    total_todo_task_schedule_count as all_tts_c,
    completed_todo_task_schedule_count as c_tts_c
FROM GOAL ;
SELECT * FROM MINDSET ;
SELECT * FROM TODO ;
SELECT * FROM TODO_TASK_SCHEDULE ;
