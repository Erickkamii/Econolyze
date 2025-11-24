import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
    env:{
        // NEXT_PUBLIC_API_BASE_URL: process.env.NEXT_PUBLIC_API_BASE_URL,
        NEXT_PUBLIC_API_BASE_URL: "http://localhost:8080/api",
    }
};

export default nextConfig;
