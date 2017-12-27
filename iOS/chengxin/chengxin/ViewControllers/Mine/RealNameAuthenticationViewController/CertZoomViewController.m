//
//  CertZoomViewController.m
//  chengxin
//
//  Created by common on 5/9/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "CertZoomViewController.h"

@interface CertZoomViewController ()

@end

@implementation CertZoomViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onEmpty:(UITapGestureRecognizer*)recognizer
{
    [self dismissViewControllerAnimated:NO completion:nil];
}
@end
