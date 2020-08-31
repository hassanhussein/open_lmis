
CREATE EXTENSION IF NOT EXISTS btree_gist;
ALTER TABLE public.data_range_flags_configuration
ADD COLUMN if not exists range numrange NOT NULL;
	
ALTER TABLE  public.data_range_flags_configuration  
DROP CONSTRAINT IF EXISTS data_range_flags_configuration_range_excl;

ALTER TABLE public.data_range_flags_configuration
ADD CONSTRAINT  data_range_flags_configuration_range_excl EXCLUDE USING gist (
       category with=, range WITH &&
       );