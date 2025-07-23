import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom"
import { useState } from "react"
import Landing from "./Landing"
import SignUpForm from "./SignUpForm"
import LoginForm from "./LoginForm"
import VerifyPage from "./VerifyPage"
import Layout from "./Layout"
import Dashboard from "./Dashboard"
import Logout from "./Logout"
import NotFound from "./NotFound"



const AppRouter = () => {
    const [user, setUser] = useState(null);
  
    const routes = [

        // Can access when logged out
        {
            path: "/",
            element: <Landing />,
        },
        {
            path: "/login",
            element: <LoginForm setUser={setUser}/>,
        },
        {
            path: "/signup",
            element: <SignUpForm/>,
        },


        // Can only access when logged in 
        {
            path: "/",
            element: <Layout />,
            children: [
                
                {
                    path: "/dashboard",
                    element: user ? <Dashboard /> : <Navigate to ="/login" />
                },
                {
                    path: "/mypets",
                    element: user ? <h1>My Pets</h1> : <Navigate to ="/login" />
                },
                {
                    path: "/addpet",
                    element: user ? <h1>Add Pet</h1> : <Navigate to ="/login" />
                },
                {
                    path: "/notes",
                    element: user ? <h1>Notes</h1> : <Navigate to ="/login" />
                },
                {
                    path: "/logout",
                    element: <Logout />
                },
                {
                    path: "*",
                    element: <NotFound/>
                }
                
            ]

        },
        {
            path: "/verify",
            element: <VerifyPage />
        },

    ]

    const router = createBrowserRouter(routes)
    
    return (
        <RouterProvider router={router} />
    )
}



export default AppRouter