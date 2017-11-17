//
//  HomeEnterpriseViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeEnterpriseViewController.h"
#import "HomeEnterpriseTableViewCell.h"

#import "Global.h"

@interface HomeEnterpriseViewController ()

@end

@implementation HomeEnterpriseViewController
@synthesize slideEnterpriseScrollView, slideEnterprisePageCtrl, homeEnterpriseTableView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.homeEnterpriseTableView registerNib:[UINib nibWithNibName:@"HomeEnterpriseTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeEnterpriseCellIdentifier"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // Set slide page time
    [self showSlideShow];
    slideEnterprisePageTimer = [NSTimer scheduledTimerWithTimeInterval:SLIDE_SECOND target:self selector:@selector(PageMove) userInfo:nil repeats:YES];
}

#pragma mark - Page Slide
- (void)showSlideShow {
    slideEnterpriseScrollView.contentSize = CGSizeMake(slideEnterpriseScrollView.frame.size.width * NUMBER_OF_SLIDE_IMAGES, slideEnterpriseScrollView.frame.size.height);
    CGRect ViewSize = slideEnterpriseScrollView.bounds;
    for(int i = 0; i <= NUMBER_OF_SLIDE_IMAGES; i++)
    {
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:ViewSize];
        imageView.contentMode = UIViewContentModeScaleToFill;
        [imageView setImage:[UIImage imageNamed:[NSString stringWithFormat:@"%d.png", (ENTERPRISE_IMAGE_START + i)]]];
        [slideEnterpriseScrollView addSubview:imageView];
        ViewSize = CGRectOffset(ViewSize, slideEnterpriseScrollView.bounds.size.width, 0);
    }
}
- (void)PageMove {
    CGFloat pageSize = slideEnterpriseScrollView.frame.size.width;
    NSInteger nCurrentPage = 0;
    // if this is the last page return
    if(slideEnterpriseScrollView.contentOffset.x >= slideEnterpriseScrollView.frame.size.width * NUMBER_OF_SLIDE_IMAGES) {
        [slideEnterpriseScrollView setContentOffset:CGPointMake(0.0, slideEnterpriseScrollView.contentOffset.y) animated:YES];
    } else {
        [slideEnterpriseScrollView setContentOffset:CGPointMake(slideEnterpriseScrollView.contentOffset.x + pageSize, slideEnterpriseScrollView.contentOffset.y) animated:YES];
        nCurrentPage = (NSInteger)(slideEnterpriseScrollView.contentOffset.x / slideEnterpriseScrollView.frame.size.width) + 1;
    }
    [slideEnterprisePageCtrl setCurrentPage:nCurrentPage];
}

#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeEnterpriseTableViewCell *homeEnterpriseTableCell = (HomeEnterpriseTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeEnterpriseCellIdentifier" forIndexPath:indexPath];
    return homeEnterpriseTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 3;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 220.f;
    return homeTableCellHeight;
}

@end
