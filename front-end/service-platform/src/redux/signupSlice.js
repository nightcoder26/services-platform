import createSlice from '@reduxjs/toolkit';

const initialState = {
    signup: 0,
}

const signupSlice = createSlice({
    name: 'signup',
    initialState,
    reducers: {
        setTrue: (state) => {
            state.signup = 1;
        },
        setFalse: (state) => {
            state.signup = 0;
        },
    }
});

export const { setTrue, setFalse } = signupSlice.actions;
export default signupSlice.reducer;