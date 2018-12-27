update product_categories set displayorder=(7+displayorder) ;
update product_categories set displayorder=1 where lower(name)  like '%'|| lower('essential medicine')||'%'	;
update product_categories set displayorder=2 where lower(name)  like '%'|| lower('MALARIA')||'%'	;
update product_categories set displayorder=3 where lower(name)  like '%'|| lower('REPRODUCTIVE HEALTH')||'%'	;
update product_categories set displayorder=4 where lower(name)  like '%'|| lower('MEDICAL & SURGICAL')||'%'	;
update product_categories set displayorder=5 where lower(name)  like '%'|| lower('VMMC')||'%'	;
update product_categories set displayorder=6 where lower(name) like '%'||lower('RAPID DIAGNOSTIC TEST')||'%'	;


----update display order for products



with cate as (
select ROW_NUMBER()  over (order by p.primaryname) rank, pp.id,pp.displayorder
	from program_products pp
	inner join products p on pp.productid = p.id

	)
	update program_products
	set displayorder=cate.rank
	from cate where program_products.id=cate.id;












