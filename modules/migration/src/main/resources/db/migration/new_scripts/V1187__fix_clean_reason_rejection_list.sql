-- Function: public.fn_new_get_timeliness_reporting_dates(integer, text, text)

ALTER TABLE public.rejections
ADD COLUMN "is_active" BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE public.rejections SET is_active='f'
WHERE code IN ('Test', 'Test2', 'Test3', 'Test4', 'Test5', 'Test6');
