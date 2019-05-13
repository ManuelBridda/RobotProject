const config = {
    floor: {
        size: { x: 40, y: 40 }
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
            centerPosition: { x: 0.2, y: 0.5},
            size: { x: 0.4, y: 0.01}
        },
        {
            name: "static-obstacle-2",
            centerPosition: { x: 0.8, y: 0.5},
            size: { x: 0.4, y: 0.01}
        }

            ]
}

export default config;