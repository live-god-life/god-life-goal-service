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

//========================================================================================

SELECT
schedule_date,
COUNT( CASE WHEN repetition_type!='NONE' then 1 end) as todoCount,
COUNT( CASE WHEN repetition_type='NONE' then 1 end) as ddayCount
FROM todo t
LEFT JOIN todo_task_schedule tts
on t.todo_id = tts.todo_task_id
WHERE
tts. schedule_date = '2022-10-01'
group by schedule_date;

select
        todotasksc1_.schedule_date as col_0_0_,
        case            when todotask0_.repetition_type<>'NONE' then 1            else null        end as col_1_0_,
        case
            when todotask0_.repetition_type='NONE' then 1
            else null
        end as col_2_0_
    from
        todo todotask0_
    left outer join
        todo_task_schedule todotasksc1_
            on todotask0_.todo_id=todotasksc1_.todo_task_id cross
    join
        goal goal2_
    where
        todotask0_.type='task'
        and todotask0_.goal_id=goal2_.goal_id
        and goal2_.user_id=1
        and year(todotasksc1_.schedule_date)*100+month(todotasksc1_.schedule_date)=202210
    group by
        todotasksc1_.schedule_date
//========================================================================================
