package woko.webtests.bootstrap

class Defect203Test extends WebTestBase {

    void testDefect203(){
        webtest('testDefect203'){
            login()
            clickLink label:"create"
            setSelectField name:"className", value:"Defect203Entity"
            clickButton name:"create"
            setInputField name:"object.intProp", value:"abcdef"
            clickButton name:"save"
            verifyText text: "The value (abcdef) entered in field Defect203 Entity Int Prop must be a valid number"
        }
    }

}
