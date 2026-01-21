import {
  Good
} from "@/types";
import { API_ENDPOINTS } from "./api-endpoints";
import { crudFactory } from "./curd-factory-ev";

export const goodClient = {
  ...crudFactory<Good, any, Partial<Good>>(API_ENDPOINTS.GOOD),
};