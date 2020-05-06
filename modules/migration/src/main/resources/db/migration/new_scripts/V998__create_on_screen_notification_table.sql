/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

CREATE TABLE on_screen_notifications
(
    id            serial primary key,
    facilityId    int references facilities (id),
    requisitionId int,
    fromUserId    int,
    toUserId      int,
    isHandled     boolean default false,
    type          varchar(200) not null,
    subject       varchar(200) not null,
    message       varchar(2000),
    url           varchar(200),
    createdBy     int,
    createdDate   date    default now(),
    modifiedBy    int,
    modifiedDate  date

)