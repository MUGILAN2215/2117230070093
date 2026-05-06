import axios from "axios";

const BASE_URL = "http://20.207.122.201/evaluation-service";
const TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiJtdWdpbGFuLnMuMjAyMy5haWRzQHJpdGNoZW5uYWkuZWR1LmluIiwiZXhwIjoxNzc4MDQ1MzMxLCJpYXQiOjE3NzgwNDQ0MzEsImlzcyI6IkFmZm9yZCBNZWRpY2FsIFRlY2hub2xvZ2llcyBQcml2YXRlIExpbWl0ZWQiLCJqdGkiOiJmYzJmODkyZC1jYzc5LTQyMTgtOWY3NC1mZmFlODZmM2QxMTIiLCJsb2NhbGUiOiJlbi1JTiIsIm5hbWUiOiJtdWdpbGFuIHMiLCJzdWIiOiJhNmRjZDAwZi0wNDkyLTQzMGMtOGZkZS04MWY0ODY0MjZiZWMifSwiZW1haWwiOiJtdWdpbGFuLnMuMjAyMy5haWRzQHJpdGNoZW5uYWkuZWR1LmluIiwibmFtZSI6Im11Z2lsYW4gcyIsInJvbGxObyI6IjIxMTcyMzAwNzAwOTMiLCJhY2Nlc3NDb2RlIjoiQlRDRHFUIiwiY2xpZW50SUQiOiJhNmRjZDAwZi0wNDkyLTQzMGMtOGZkZS04MWY0ODY0MjZiZWMiLCJjbGllbnRTZWNyZXQiOiJQbVpteUR2RnpLeWZ4ZGVrIn0.rq-yNM33jVdiFTG05x3K80ve5TnlurE4DeiJt5PbvK0";

type Stack = "backend" | "frontend";
type Level = "debug" | "info" | "warn" | "error" | "fatal";
type Package =
  | "cache" | "controller" | "cron_job" | "db" | "domain"
  | "handler" | "repository" | "route" | "service"
  | "api" | "component" | "hook" | "page" | "state" | "style"
  | "auth" | "config" | "middleware" | "utils";

export async function Log(
  stack: Stack,
  level: Level,
  pkg: Package,
  message: string
): Promise<void> {
  try {
    await axios.post(
      `${BASE_URL}/logs`,
      { stack, level, package: pkg, message },
      {
        headers: {
          Authorization: `Bearer ${TOKEN}`,
          "Content-Type": "application/json",
        },
      }
    );
  } catch (err) {
    // silent fail
  }
}