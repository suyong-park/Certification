import axios from "axios";

const api = axios.create({
  baseURL: "http://15.164.49.117/certifi/",
  //params: {
  //api_key: "152bb4bf61854f542b933862d3d9f324",
  //language: "en-US",
  //},
});

export const moviesApi = {
  Ccategory: () => api.get("certification_category_data.php"),
  certification: () => api.get("certification_data.php"),
  Jcategory: () => api.get("job_category_data.php"),
};
