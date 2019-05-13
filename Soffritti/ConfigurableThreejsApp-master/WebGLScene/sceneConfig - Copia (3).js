const config = {
    floor: {
        size: { x: 30, y: 30 }
    },
    player: {
        position: { x: 0.04, y: 0.04 },
        speed: 0.2
    },
    sonars: [
        {
            name: "sonar-1",
            position: { x: 0.02, y: 0.02 },
            senseAxis: { x: true, y: true }
        }, 
        {
            name: "sonar-2",
            position: { x: 0.98, y: 0.98 },
            senseAxis: { x: true, y: true }
        }
    ],
    movingObstacles: [
            ],
    staticObstacles: [

        {
            name: "static-obstacle-1",
            centerPosition: { x: 0.28, y: 0.38},
            size: { x: 0.02, y: 0.158}
        },
        {
            name: "static-obstacle-2",
            centerPosition: { x: 0.15, y: 0.44},
            size: { x: 0.28, y: 0.031}
        },
{
            name: "static-obstacle-3",
            centerPosition: { x: 0.58, y: 0.07},
            size: { x: 0.13, y: 0.13}
        },
        {
            name: "static-obstacle-4",
            centerPosition: { x: 0.603, y: 0.84},
            size: { x: 0.02, y: 0.31}
        }

            ]
}

export default config;