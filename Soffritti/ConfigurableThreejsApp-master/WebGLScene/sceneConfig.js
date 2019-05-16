const config = {
    floor: {
        size: { x: 21, y: 21 }
    },
    player: {
        position: { x: 0.04, y: 0.04 },
        speed: 0.2
    },
    sonars: [
        {
            name: "sonar-1",
            position: { x: 0.02, y: 0.02 },
            senseAxis: { x: false, y: false }
        }, 
        {
            name: "sonar-2",
            position: { x: 0.94, y: 0.94 },
            senseAxis: { x: true, y: true }
        }
    ],
    movingObstacles: [
                
	     	   
	
            ],

    staticObstacles: [

        {
            name: "static-obstacle-1",
            centerPosition: { x: 0.12, y: 0.64},
            size: { x: 0.25, y: 0.01}
        },
        {
            name: "static-obstacle-2",
            centerPosition: { x: 0.88, y: 0.5},
            size: { x: 0.24, y: 0.01}
        },
{
            name: "static-obstacle-3",
            centerPosition: { x: 0.25, y: 0.51},
            size: { x: 0.01, y: 0.24}
        },
{
            name: "static-obstacle-4",
            centerPosition: { x: 0.12, y: 0.39},
            size: { x: 0.25, y: 0.01}
        },

            ]
}

export default config;