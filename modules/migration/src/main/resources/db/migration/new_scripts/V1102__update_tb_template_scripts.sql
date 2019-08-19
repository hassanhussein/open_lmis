update program_rnr_columns set label='Loss & Adjustments (B)' where masterColumnId=(select id from master_rnr_columns where name='lossesAndAdjustments') and
programId=(select id from programs where code = 'TB&LEPROSY')
