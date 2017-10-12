--
-- Copyright 2010-2013 Axel Fontaine and the many contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--         http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
DELIMITER $$

CREATE FUNCTION TEST_FUNC ( PARAM1 INTEGER ) RETURNING INTEGER;
  RETURN PARAM1;
END FUNCTION;
$$
CREATE FUNCTION TEST_FUNC ( PARAM1 INTEGER, PARAM2 INTEGER ) RETURNING INTEGER;
  RETURN PARAM1+PARAM2;
END FUNCTION;
$$
CREATE FUNCTION TEST_FUNC ( PARAM1 INTEGER, PARAM2 INTEGER, PARAM3 INTEGER )RETURNING INTEGER;
  RETURN PARAM1+PARAM2+PARAM3;
END FUNCTION;
$$
--DROP FUNCTION TEST_FUNC ( INTEGER);
--DROP FUNCTION TEST_FUNC ( INTEGER,INTEGER);
--DROP FUNCTION TEST_FUNC ( INTEGER,INTEGER,INTEGER);