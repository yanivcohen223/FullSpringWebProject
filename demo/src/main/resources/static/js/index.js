    async function drow_classroom_table() {
        const response = await fetch("http://localhost:8085/api/class");
        const data = await response.json();
        for(let i = 0; i < data.length; i++) {
            $('#class_table').append(`
                <tr>
                    <td>${data[i].id}</td>
                    <td>${data[i].numberofstudents}</td>
                    <td>${data[i].classavg}</td>
                    <td>${data[i].whichclass}</td>

                </tr>
                `)
        }
    }

    async function drow_student_table() {
        const response = await fetch("http://localhost:8085/api/students");
        const data = await response.json();
        for(let i = 0; i < data.length; i++) {
            $('#student_table').append(`
                <tr>
                    <td>${data[i].id}</td>
                    <td>${data[i].last_name}</td>
                    <td>${data[i].first_name}</td>
                    <td>${data[i].avgGrade}</td>
                    <td>${data[i].gender}</td>
                    <td>${data[i].class_id}</td>
                </tr>
                `)
        }
    }
drow_classroom_table()
drow_student_table()