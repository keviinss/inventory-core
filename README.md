# inventory-core

NOTE :
- if inventory added with type T (Top Up) then stock items will increase and if type W (Withdrawal) then stock items will decreased. 
- if item ordered it will also reduce/deduct the stock of the item. 

Spring Boot Application that provides the following API endpoints:

1. ITEM
when listing / get item should have option to display remaining stock of each item	
		
	- Get
	- Listing with pagination
	- Save
	- Edit
	- Delete
 
2. INVENTORY
used to T (Top Up) or W (Withdrawal) stock/inventory items	
	
	- Get
	- Listing with pagination
	- Save
	- Edit
	- Delete

3. ORDER	
every time there is a new ORDER you need to check the remaining stock of the item, if the item stock is insufficient, an insufficient stock message will appear

	- Get
	- Listing with pagination
	- Save
	- Edit
	- Delete

