import { createBrowserRouter } from "react-router-dom";
import { Suspense, lazy } from "react";
import PrivateRoute from './components/PrivateRoute';

const Loading = <div>Loading....</div>
const Home = lazy(() => import("./pages/Home"));
const SignUp = lazy(() => import("./pages/SignUp"));
const Login = lazy(() => import("./pages/Login"));
const Gathering = lazy(() => import("./pages/Gathering"));
const NewGathering = lazy(() => import("./pages/NewGathering"));
const LoginResponse = lazy(() => import("./pages/LoginResponse"));

const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <Suspense fallback={Loading}>
        <PrivateRoute>
          <Home />
        </PrivateRoute>
      </Suspense>
    ),
  },
  {
    path: "/sign-up",
    element: (
      <Suspense fallback={Loading}>
        <PrivateRoute>
          <SignUp />
        </PrivateRoute>
      </Suspense>
    ),
  },
  {
    path: "/login",
    element: (
      <Suspense fallback={Loading}>
        <Login />
      </Suspense>
    ),
  },
  {
    path: "/gathering",
    element: (
      <Suspense fallback={Loading}>
        <PrivateRoute>
          <Gathering />
        </PrivateRoute>
      </Suspense>
    ),
  },
  {
    path: "/gathering/new",
    element: (
      <Suspense fallback={Loading}>
        <PrivateRoute>
          <NewGathering />
        </PrivateRoute>
      </Suspense>
    ),
  },
  {
    path: "/login-response",
    element: (
      <Suspense fallback={Loading}>
        <LoginResponse />
      </Suspense>
    ),
  },
]);

export default router;
